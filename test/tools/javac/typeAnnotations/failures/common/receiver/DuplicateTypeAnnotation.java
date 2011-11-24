/*
 * @test /nodynamiccopyright/
 * @bug 6843077
 * @summary check for duplicate annotations in receiver
 * @author Mahmood Ali
 * @compile/fail/ref=DuplicateTypeAnnotation.out -XDrawDiagnostics -source 1.8 DuplicateTypeAnnotation.java
 */

class DuplicateTypeAnnotation {
  void test() @A @A { }
}

@interface A { }
