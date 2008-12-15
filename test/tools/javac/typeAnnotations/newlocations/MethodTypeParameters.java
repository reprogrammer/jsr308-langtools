/*
 * Copyright 2008 Sun Microsystems, Inc.  All Rights Reserved.
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
 * Please contact Sun Microsystems, Inc., 4150 Network Circle, Santa Clara,
 * CA 95054 USA or visit www.sun.com if you need additional information or
 * have any questions.
 */

/*
 * @test
 * @summary new type annotation location: method type parameter bounds
 * @author Mahmood Ali
 * @compile MethodTypeParameters.java
 */

class UnscopedUnmodified {
  <K extends @A String> void methodExtends() {}
  <K extends @A Parameterized<@B String>> void nestedExtends() {}
  <K extends @A String, V extends @A Parameterized<@B String>> void dual() {}
  <K extends String, V extends Parameterized<@B String>> void dualOneAnno() {}
}

class PublicModifiedMethods {
  public final <K extends @A String> void methodExtends() {}
  public final <K extends @A Parameterized<@B String>> void nestedExtends() {}
  public final <K extends @A String, V extends @A Parameterized<@B String>> void dual() {}
  public final <K extends String, V extends Parameterized<@B String>> void dualOneAnno() {}
}

class Parameterized<K> { }
@interface A { }
