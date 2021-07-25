package io.rv.restdemo.app.domain;

import java.net.MalformedURLException;
import java.net.URL;

public final class URLUtil {

    static URL createURL(final String urlStr) {
        try {
            return new URL(urlStr);
        } catch (final MalformedURLException e) {
            throw new RuntimeException("Failed to create URL for: " + urlStr, e);
        }
    }

}
