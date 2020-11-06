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
package com.artipie.asto.cache;

import com.artipie.asto.Content;
import com.artipie.asto.Key;
import com.artipie.asto.ext.Digests;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import org.apache.commons.codec.binary.Hex;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

/**
 * Test case for {@link DigestVerification}.
 *
 * @since 0.25
 * @checkstyle MagicNumberCheck (500 lines)
 */
final class DigestVerificationTest {

    @Test
    void validatesCorrectDigest() throws Exception {
        final boolean result = new DigestVerification(
            Digests.MD5,
            Hex.decodeHex("5289df737df57326fcdd22597afb1fac")
        ).validate(
            new Key.From("any"),
            () -> CompletableFuture.supplyAsync(
                () -> Optional.of(new Content.From(new byte[]{1, 2, 3}))
            )
        ).toCompletableFuture().get();
        MatcherAssert.assertThat(result, Matchers.is(true));
    }

    @Test
    void doesntValidatesIncorrectDigest() throws Exception {
        final boolean result = new DigestVerification(
            Digests.MD5, new byte[16]
        ).validate(
            new Key.From("other"),
            () -> CompletableFuture.supplyAsync(
                () -> Optional.of(new Content.From(new byte[]{1, 2, 3}))
            )
        ).toCompletableFuture().get();
        MatcherAssert.assertThat(result, Matchers.is(false));
    }

    @Test
    void doesntValidateAbsentContent() throws Exception {
        MatcherAssert.assertThat(
            new DigestVerification(
                Digests.MD5, new byte[16]
            ).validate(
                new Key.From("something"),
                () -> CompletableFuture.supplyAsync(Optional::empty)
            ).toCompletableFuture().get(),
            Matchers.is(false)
        );
    }
}
