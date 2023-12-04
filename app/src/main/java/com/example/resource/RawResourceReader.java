package com.example.resource;

import android.content.res.Resources;

import java.io.InputStream;
import java.util.Scanner;

public class RawResourceReader {

    private final Resources resources;

    public RawResourceReader(Resources resources) {
        this.resources = resources;
    }

    public String readRawResource(int resourceId) {
        InputStream inputStream = resources.openRawResource(resourceId);
        Scanner scanner = new Scanner(inputStream).useDelimiter("\\A");
        return scanner.hasNext() ? scanner.next() : "";
    }
}
