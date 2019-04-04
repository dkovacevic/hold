// Wire
// Copyright (C) 2016 Wire Swiss GmbH
//
// This program is free software: you can redistribute it and/or modify
// it under the terms of the GNU General Public License as published by
// the Free Software Foundation, either version 3 of the License, or
// (at your option) any later version.
//
// This program is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
// GNU General Public License for more details.
//
// You should have received a copy of the GNU General Public License
// along with this program. If not, see http://www.gnu.org/licenses/.
//

package com.wire.bots.hold;

import com.wire.bots.hold.model.Config;
import com.wire.bots.sdk.MessageHandlerBase;
import com.wire.bots.sdk.Server;
import com.wire.bots.sdk.factories.StorageFactory;
import com.wire.bots.sdk.state.RedisState;
import io.dropwizard.setup.Environment;

public class Service extends Server<Config> {
    public static void main(String[] args) throws Exception {
        new Service().run(args);
    }

    @Override
    protected void initialize(Config config, Environment env) {
        env.jersey().setUrlPattern("/hold/*");
    }

    @Override
    protected MessageHandlerBase createHandler(Config config, Environment env) {
        return new MessageHandler(new Database(config.storage));
    }

    @Override
    protected void onRun(Config config, Environment env) {
        Database database = new Database(config.storage);

        LoginResource loginResource = new LoginResource(client,
                database,
                getCryptoFactory(config),
                config);

        addResource(loginResource, env);

        Thread thread = new Thread(new NotificationProcessor(client, config));
        thread.start();
    }

    /**
     * Instructs the framework to use Storage Service for the state.
     * Remove this override in order to use local File system storage
     *
     * @param config Config
     * @return Storage
     */
    @Override
    protected StorageFactory getStorageFactory(Config config) {
        return botId -> new RedisState(botId, config.db);
    }

    /**
     * Instructs the framework to use Crypto Service for the crypto keys.
     * Remove this override in order to store cryptobox onto your local File system
     *
     * @param config Config
     * @return CryptoFactory
     */
//    @Override
//    protected CryptoFactory getCryptoFactory(Config config) {
//        return (botId) -> {
//            RedisStorage storage = new RedisStorage(config.db.host, config.db.port, config.db.password);
//            return new CryptoDatabase(botId, storage);
//        };
//    }
}
