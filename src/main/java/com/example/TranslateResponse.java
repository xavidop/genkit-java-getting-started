/*
 * Copyright 2026 Xavier Portilla Edo
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package com.example;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;

/**
 * Structured output for the translate flow.
 */
public class TranslateResponse {

    @JsonProperty(required = true)
    @JsonPropertyDescription("The original text that was translated")
    private String originalText;

    @JsonProperty(required = true)
    @JsonPropertyDescription("The translated text")
    private String translatedText;

    @JsonProperty(required = true)
    @JsonPropertyDescription("The target language")
    private String language;

    public TranslateResponse() {}

    public TranslateResponse(String originalText, String translatedText, String language) {
        this.originalText = originalText;
        this.translatedText = translatedText;
        this.language = language;
    }

    public String getOriginalText() {
        return originalText;
    }

    public void setOriginalText(String originalText) {
        this.originalText = originalText;
    }

    public String getTranslatedText() {
        return translatedText;
    }

    public void setTranslatedText(String translatedText) {
        this.translatedText = translatedText;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    @Override
    public String toString() {
        return String.format(
            "TranslateResponse{originalText='%s', translatedText='%s', language='%s'}",
            originalText, translatedText, language);
    }
}
