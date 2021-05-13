/*
 * Copyright Amazon.com, Inc. or its affiliates. All Rights Reserved.
 * SPDX-License-Identifier: Apache-2.0
 */

package com.aws.greengrass.network;

import com.aws.greengrass.util.ProxyUtils;
import software.amazon.awssdk.http.SdkHttpClient;
import software.amazon.awssdk.http.apache.ApacheHttpClient;


public class HttpClientProvider {
    /**
     * Provides a SdkHttpClient with Proxy configuration if it is available or a regular ApacheHttpClient. Invoker
     * should close client properly as this class doesn't close the provided client.
     *
     * @return SdkHttpClient for making http calls
     */
    public SdkHttpClient getSdkHttpClient() {
        SdkHttpClient proxyClient = ProxyUtils.getSdkHttpClient();

        if (proxyClient != null) {
            // use Proxy client if proxy is configured
            return proxyClient;
        }

        // TODO investigate making ApacheHttpClient into Singleton and reuse across the system.
        return ApacheHttpClient.builder().build();
    }

}
