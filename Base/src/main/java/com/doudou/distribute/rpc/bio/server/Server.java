package com.doudou.distribute.rpc.bio.server;

import java.io.IOException;

public interface Server {
    void start() throws IOException;

    void register(Class serviceInterface, Class impl);
}
