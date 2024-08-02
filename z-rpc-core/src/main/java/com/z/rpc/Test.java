package com.z.rpc;

import com.z.rpc.serializer.Serializer;

import java.util.ServiceLoader;

public class Test {
    public static void main(String[] args) {

        ServiceLoader<Serializer> load = ServiceLoader.load(Serializer.class);
        for (Serializer serializer : load) {
            System.out.println(serializer);
        }
    }
}
