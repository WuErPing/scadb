//=========================================================================\\
//     _____               _ _
//    / ____|             | | |
//   | (___   ___ __ _  __| | |__
//    \___ \ / __/ _` |/ _` | '_ \
//    ____) | (_| (_| | (_| | |_) |
//   |_____/ \___\__,_|\__,_|_.__/

// Copyright 2016 The Scadb Authors. All rights reserved.
//
// Licensed under the Apache License, Version 2.0 (the "License"): you may
// not use this file except in compliance with the License. You may obtain
// a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
// WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
// License for the specific language governing permissions and limitations
// under the License.
//=========================================================================\\
package org.herry2038.scadb.scadb.handler.newdecorator

import com.alibaba.druid.sql.ast.SQLStatement
import org.herry2038.scadb.mysql.server.MySQLServerConnectionHandler
import org.herry2038.scadb.scadb.handler.executor.{ScadbStatementDdl, ScadbStatement}

class DecoratorInterfaceDdl(sql: String, statements: java.util.List[SQLStatement], startTime: Long) extends DecoratorInterface(statements.get(0), startTime) {
  override def readable(): Boolean = ???

  override def tableName(): String = ???

  override def setPartition(partition: String, prefix: String): Unit = ???

  override def decorator(handler: MySQLServerConnectionHandler): ScadbStatement = {
    return new ScadbStatementDdl(handler, sql)
  }
}
