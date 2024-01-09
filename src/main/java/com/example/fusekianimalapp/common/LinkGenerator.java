package com.example.fusekianimalapp.common;

public class LinkGenerator {
    public static String generateLinks(String input) {
        // Replace [[link text]] with <a href="#" onclick="navigateToLink('http://example.com')">link text</a>
        return input.replaceAll("\\[\\[(.+?)\\]\\]", "<a href=\"#\" onclick=\"navigateToLink('http://example.com')\">$1</a>");
    }
}
