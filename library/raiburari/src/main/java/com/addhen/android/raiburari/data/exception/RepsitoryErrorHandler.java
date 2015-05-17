/*
 * Copyright (c) 2015 Henry Addo
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.addhen.android.raiburari.data.exception;

import com.addhen.android.raiburari.domain.exception.ErrorHandler;

/**
 * Exception handler for Repository errors
 *
 * @author Henry Addo
 */
public class RepsitoryErrorHandler implements ErrorHandler {

    private final Exception mException;

    public RepsitoryErrorHandler(Exception exception) {
        mException = exception;
    }

    @Override
    public Exception getException() {
        return mException;
    }

    @Override
    public String getErrorMessage() {
        return mException.getMessage();
    }
}
