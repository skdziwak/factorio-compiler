package com.skdziwak.factoriolang.compilation.interfaces;

import com.skdziwak.factoriolang.compilation.CompilationState;

public interface PostCompilable {
    void postCompile(CompilationState state);
}
