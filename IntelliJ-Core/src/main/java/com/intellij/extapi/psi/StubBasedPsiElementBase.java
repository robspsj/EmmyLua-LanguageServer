/*
 * Copyright 2000-2016 JetBrains s.r.o.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.intellij.extapi.psi;

import com.intellij.lang.ASTNode;
import com.intellij.psi.StubBasedPsiElement;
import com.intellij.psi.stubs.IStubElementType;
import com.intellij.psi.stubs.StubElement;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NotNull;

public class StubBasedPsiElementBase<T extends StubElement> extends ASTWrapperPsiElement {

    public StubBasedPsiElementBase(@NotNull T stub, @NotNull IStubElementType nodeType) {
        super(null);
    }

    public StubBasedPsiElementBase(@NotNull ASTNode node) {
        super(node);
    }

    public StubBasedPsiElementBase(T stub, IElementType nodeType, ASTNode node) {
        super(node);
    }

    public IStubElementType getElementType() {
        return null;
    }

    public T getStub() {
        return null;
    }
}
