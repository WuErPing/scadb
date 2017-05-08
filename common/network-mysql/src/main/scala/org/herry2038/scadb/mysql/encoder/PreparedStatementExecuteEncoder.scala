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
package org.herry2038.scadb.mysql.encoder

import io.netty.buffer.{ByteBuf, Unpooled}
import org.herry2038.scadb.mysql.column.ColumnTypes
import org.herry2038.scadb.mysql.binary.BinaryRowEncoder
import org.herry2038.scadb.mysql.message.client.{PreparedStatementExecuteMessage, ClientMessage}
import org.herry2038.scadb.db.util.ByteBufferUtils
import org.herry2038.scadb.mysql.binary.BinaryRowEncoder
import org.herry2038.scadb.mysql.column.ColumnTypes

class PreparedStatementExecuteEncoder( rowEncoder : BinaryRowEncoder ) extends MessageEncoder {

  def encode(message: ClientMessage): ByteBuf = {
    val m = message.asInstanceOf[PreparedStatementExecuteMessage]

    val buffer = ByteBufferUtils.packetBuffer()
    buffer.writeByte( m.kind )
    buffer.writeBytes(m.statementId)
    buffer.writeByte(0x00) // no cursor
    buffer.writeInt(1)

    if ( m.parameters.isEmpty ) {
      buffer
    } else {
      Unpooled.wrappedBuffer(buffer, encodeValues(m.values, m.valuesToInclude))
    }

  }

  private[encoder] def encodeValues( values : Seq[Any], valuesToInclude: Set[Int] ) : ByteBuf = {
    val nullBitsCount = (values.size + 7) / 8
    val nullBits = new Array[Byte](nullBitsCount)
    val bitMapBuffer = ByteBufferUtils.mysqlBuffer(1 + nullBitsCount)
    val parameterTypesBuffer = ByteBufferUtils.mysqlBuffer(values.size * 2)
    val parameterValuesBuffer = ByteBufferUtils.mysqlBuffer()

    var index = 0

    while ( index < values.length ) {
      val value = values(index)
      if ( value == null || value == None ) {
        nullBits(index / 8) = (nullBits(index / 8) | (1 << (index & 7))).asInstanceOf[Byte]
        parameterTypesBuffer.writeShort(ColumnTypes.FIELD_TYPE_NULL)
      } else {
        value match {
          case Some(v) => encodeValue(parameterTypesBuffer, parameterValuesBuffer, v, valuesToInclude(index))
          case _ => encodeValue(parameterTypesBuffer, parameterValuesBuffer, value, valuesToInclude(index))
        }
      }
      index += 1
    }

    bitMapBuffer.writeBytes(nullBits)
    if ( values.size > 0 ) {
      bitMapBuffer.writeByte(1)
    } else {
      bitMapBuffer.writeByte(0)
    }

    Unpooled.wrappedBuffer( bitMapBuffer, parameterTypesBuffer, parameterValuesBuffer )
  }

  private def encodeValue(parameterTypesBuffer: ByteBuf, parameterValuesBuffer: ByteBuf, value: Any, includeValue: Boolean) : Unit = {
    val encoder = rowEncoder.encoderFor(value)
    parameterTypesBuffer.writeShort(encoder.encodesTo)
    if (includeValue)
      encoder.encode(value, parameterValuesBuffer)
  }
  
  
  def decode(buffer: ByteBuf): ClientMessage = {
    buffer.readByte() // ClientMessage.PreparedStatementExecute
    val statementId = Array[Byte]( buffer.readByte(), buffer.readByte(), buffer.readByte(), buffer.readByte() )
    
    buffer.readByte  // flags
    buffer.readInt   // iteration-count
    
	// TODO
    null
  }

}