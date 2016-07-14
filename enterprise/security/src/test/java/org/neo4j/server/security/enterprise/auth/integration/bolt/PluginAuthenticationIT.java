/*
 * Copyright (c) 2002-2016 "Neo Technology,"
 * Network Engine for Objects in Lund AB [http://neotechnology.com]
 *
 * This file is part of Neo4j.
 *
 * Neo4j is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package org.neo4j.server.security.enterprise.auth.integration.bolt;

import org.junit.Test;

import java.util.Map;
import java.util.function.Consumer;

import org.neo4j.graphdb.config.Setting;

import static org.neo4j.helpers.collection.MapUtil.map;

public class PluginAuthenticationIT extends EnterpriseAuthenticationTestBase
{
    @Override
    protected Consumer<Map<Setting<?>, String>> getSettingsFunction()
    {
        return super.getSettingsFunction().andThen( pluginOnlyAuthSettings );
    }

    @Test
    public void shouldAuthenticateWithTestAuthenticationPlugin() throws Throwable
    {
        assertConnectionSucceeds( map( "principal", "neo4j", "credentials", "neo4j", "scheme", "basic", "realm",
                "TestAuthenticationPlugin" ) );
    }

    @Test
    public void shouldAuthenticateAndAuthorizeWithTestAuthPlugin() throws Throwable
    {
        assertConnectionSucceeds(
                map( "principal", "neo4j", "credentials", "neo4j", "scheme", "basic", "realm", "TestAuthPlugin" ) );
        assertReadSucceeds();
        assertWriteFails( "neo4j" );
    }

    @Test
    public void shouldAuthenticateAndAuthorizeWithTestCombinedAuthPlugin() throws Throwable
    {
        assertConnectionSucceeds( map( "principal", "neo4j", "credentials", "neo4j", "scheme", "basic", "realm",
                "TestCombinedAuthPlugin" ) );
        assertReadSucceeds();
        assertWriteFails( "neo4j" );
    }
}
