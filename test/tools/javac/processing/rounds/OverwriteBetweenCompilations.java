/*
 * Copyright (c) 2014, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 */

/*
 * @test
 * @bug 8038455
 * @summary Verify that annotation processor can overwrite source and class files it generated
 *          during previous compilations, and that the Symbols are updated appropriatelly.
 * @library /tools/javac/lib/
 * @clean *
 * @build OverwriteBetweenCompilations ToolBox JavacTestingAbstractProcessor
 * @compile/ref=OverwriteBetweenCompilations_1.out -processor OverwriteBetweenCompilations -Apass=1 -parameters -XDrawDiagnostics OverwriteBetweenCompilationsSource.java
 * @compile/ref=OverwriteBetweenCompilations_2.out -processor OverwriteBetweenCompilations -Apass=2 -parameters -XDrawDiagnostics OverwriteBetweenCompilationsSource.java
 * @compile/ref=OverwriteBetweenCompilations_3.out -processor OverwriteBetweenCompilations -Apass=3 -parameters -XDrawDiagnostics OverwriteBetweenCompilationsSource.java
 */

import java.io.*;
import java.util.*;

import javax.annotation.processing.*;
import javax.lang.model.element.*;
import javax.tools.*;

import com.sun.tools.javac.processing.JavacProcessingEnvironment;
import com.sun.tools.javac.processing.PrintingProcessor.PrintingElementVisitor;
import com.sun.tools.javac.util.Log;
import com.sun.tools.javac.util.Log.WriterKind;

@SupportedOptions("pass")
public class OverwriteBetweenCompilations extends JavacTestingAbstractProcessor {
    int round = 1;
    public boolean process(Set<? extends TypeElement> annotations,
                           RoundEnvironment roundEnv) {
        Log log = Log.instance(((JavacProcessingEnvironment) processingEnv).getContext());
        PrintWriter pw = log.getWriter(WriterKind.NOTICE);

        pw.println("round: " + round);

        TypeElement generatedSource =
                processingEnv.getElementUtils().getTypeElement("GeneratedSource");

        if (generatedSource != null) {
            new PrintingElementVisitor(pw, processingEnv.getElementUtils()).visit(generatedSource);
            pw.flush();
        }

        TypeElement generatedClass =
                processingEnv.getElementUtils().getTypeElement("GeneratedClass");

        if (generatedClass != null) {
            new PrintingElementVisitor(pw, processingEnv.getElementUtils()).visit(generatedClass);
            pw.flush();
        }

        int pass = Integer.parseInt(processingEnv.getOptions().get("pass"));

        if (round++ == 1) {
            try (Writer out = filer.createSourceFile("GeneratedSource").openWriter()) {
                String code = pass != 2 ? GENERATED_INIT : GENERATED_UPDATE;
                code = code.replace("NAME", "GeneratedSource");
                out.write(code);
            } catch (IOException e) {
                processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, e.toString());
            }
            try (OutputStream out = filer.createClassFile("GeneratedClass").openOutputStream()) {
                String code = pass != 2 ? GENERATED_INIT : GENERATED_UPDATE;
                code = code.replace("NAME", "GeneratedClass");
                ToolBox.JavaToolArgs args =
                        new ToolBox.JavaToolArgs().appendArgs("-parameters").setSources(code);
                Map<String, byte[]> codeMap = ToolBox.compile(args);
                out.write(codeMap.get("GeneratedClass"));
            } catch (IOException | ToolBox.CommandExecutionException e) {
                processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, e.toString());
            }
        }

        return false;
    }

    //the initial generated class - "NAME" will be replaced with either "GeneratedSource" or
    //"GeneratedClass" while generating the class:
    private static final String GENERATED_INIT =
            "@Deprecated\n" +
            "public class NAME<T extends CharSequence> extends java.util.ArrayList<String>\n" +
            "                                          implements Runnable {\n" +
            "    public void test(int a) { }\n" +
            "    public void run() { }\n" +
            "}";

    //generated class update- "NAME" will be replaced with either "GeneratedSource" or
    //"GeneratedClass" while generating the class:
    private static final String GENERATED_UPDATE =
            "@javax.annotation.processing.SupportedAnnotationTypes(\"*\")\n" +
            "public abstract class NAME<E extends Number> extends java.util.LinkedList<Number>" +
            "                                             implements Runnable, CharSequence {\n" +
            "    public void test(long a) { }\n" +
            "}";
}
