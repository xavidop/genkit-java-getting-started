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

import com.google.genkit.Genkit;
import com.google.genkit.GenkitOptions;
import com.google.genkit.ai.GenerateOptions;
import com.google.genkit.ai.GenerationConfig;
import com.google.genkit.plugins.googlegenai.GoogleGenAIPlugin;
import com.google.genkit.plugins.jetty.JettyPlugin;
import com.google.genkit.plugins.jetty.JettyPluginOptions;

/**
 * Genkit Java + Gemini Getting Started Application.
 *
 * <p>This application demonstrates how easy it is to build generative AI features
 * in Java using Genkit and Google's Gemini models, ready to deploy on Google Cloud Run.
 *
 * <p>It uses typed Java classes as flow inputs/outputs and structured output
 * generation with {@code outputClass}, so Gemini returns data directly as
 * typed Java objects — no manual JSON parsing needed.
 *
 * <p>To run locally:
 * <ol>
 *   <li>Set the GOOGLE_GENAI_API_KEY environment variable</li>
 *   <li>Run: genkit start -- mvn compile exec:java</li>
 *   <li>Open the Dev UI at http://localhost:4000</li>
 * </ol>
 */
public class App {

    public static void main(String[] args) throws Exception {
        System.out.println("=== Genkit Java + Gemini - Getting Started ===\n");

        // Read the port from the PORT environment variable (Cloud Run sets this)
        int port = Integer.parseInt(System.getenv().getOrDefault("PORT", "8080"));

        // Create the Jetty HTTP server plugin
        JettyPlugin jetty = new JettyPlugin(
            JettyPluginOptions.builder()
                .port(port)
                .build()
        );

        // Initialize Genkit — that's it, this is all you need!
        Genkit genkit = Genkit.builder()
            .options(GenkitOptions.builder()
                .devMode(true)
                .reflectionPort(3100)
                .build())
            .plugin(GoogleGenAIPlugin.create())
            .plugin(jetty)
            .build();

        // --- Define your AI flows ---

        // Translate flow with typed input (TranslateRequest) and typed output (TranslateResponse)
        defineTranslateFlow(genkit);

        System.out.println("Server started on http://localhost:" + port);
        System.out.println("\nAvailable flows:");
        System.out.println("  POST /api/flows/translate  - Translate text (structured input/output)");
        System.out.println("\nPress Ctrl+C to stop the server.");

        // Start the server and block
        jetty.start();
    }

    /**
     * A flow that translates text to a specified language.
     *
     * <p>Uses typed classes for both the flow signature and the LLM output:
     * <ul>
     *   <li>Flow input: {@link TranslateRequest} — typed Java class</li>
     *   <li>Flow output: {@link TranslateResponse} — typed Java class</li>
     *   <li>LLM output: {@code outputClass(TranslateResponse.class)} — Gemini returns
     *       structured JSON that is automatically deserialized into a TranslateResponse</li>
     * </ul>
     */
    private static void defineTranslateFlow(Genkit genkit) {
        genkit.defineFlow(
            "translate",
            TranslateRequest.class,
            TranslateResponse.class,
            (ctx, request) -> {
                String prompt = String.format(
                    "Translate the following text to %s.\n\nText: %s",
                    request.getLanguage(), request.getText()
                );

                return genkit.generate(
                    GenerateOptions.<TranslateResponse>builder()
                        .model("googleai/gemini-3-flash-preview")
                        .prompt(prompt)
                        .outputClass(TranslateResponse.class)
                        .config(GenerationConfig.builder()
                            .temperature(0.1)
                            .build())
                        .build()
                );
            }
        );
    }
}
