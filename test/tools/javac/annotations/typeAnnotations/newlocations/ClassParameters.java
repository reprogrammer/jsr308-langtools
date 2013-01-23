/*
 * Copyright (c) 2008, Oracle and/or its affiliates. All rights reserved.
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

import java.lang.annotation.*;

/*
 * @test
 * @bug 6843077 8006775
 * @summary new type annotation location: class type parameter bounds
 * @author Mahmood Ali
 * @compile ClassParameters.java
 */
class Unannotated<K> { }

class ExtendsBound<K extends @A String> { }
class ExtendsGeneric<K extends @A Unannotated<@B String>> { }
class TwoBounds<K extends @A String, V extends @B String> { }

class Complex1<K extends @A String&Runnable> { }
class Complex2<K extends String & @B Runnable> { }
class ComplexBoth<K extends @A String & @A Runnable> { }

class Outer {
  void inner() {
    class Unannotated<K> { }

    class ExtendsBound<K extends @A String> { }
    class ExtendsGeneric<K extends @A Unannotated<@B String>> { }
    class TwoBounds<K extends @A String, V extends @B String> { }

    class Complex1<K extends @A String&Runnable> { }
    class Complex2<K extends String & @B Runnable> { }
    class ComplexBoth<K extends @A String & @A Runnable> { }
  }
}

@Target({ElementType.TYPE_USE, ElementType.TYPE_PARAMETER})
@interface A { }
@Target({ElementType.TYPE_USE, ElementType.TYPE_PARAMETER})
@interface B { }
