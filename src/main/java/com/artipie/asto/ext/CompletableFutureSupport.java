/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2020 artipie.com
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included
 * in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NON-INFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.artipie.asto.ext;

import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

/**
 * Support of new {@link CompletableFuture} API for JDK 1.8.
 * @param <T> Future type
 * @since 0.33
 */
public abstract class CompletableFutureSupport<T> implements Supplier<CompletableFuture<T>> {

    /**
     * Supplier wrap.
     */
    private final Supplier<? extends CompletableFuture<T>> wrap;

    /**
     * New wrapped future supplier.
     * @param wrap Supplier to wrap
     */
    protected CompletableFutureSupport(final Supplier<? extends CompletableFuture<T>> wrap) {
        this.wrap = wrap;
    }

    @Override
    public final CompletableFuture<T> get() {
        return this.wrap.get();
    }

    /**
     * Failed completable future supplier.
     * @param <T> Future type
     * @since 0.33
     */
    public static final class Failed<T> extends CompletableFutureSupport<T> {
        /**
         * New failed future.
         * @param err Failure exception
         */
        public Failed(final Exception err) {
            super(() -> {
                final CompletableFuture<T> future = new CompletableFuture<>();
                future.completeExceptionally(err);
                return future;
            });
        }
    }

}
