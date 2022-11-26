package infraestructure.messaging

import com.fasterxml.jackson.core.`type`.TypeReference

case class Message(key: String, value: String, createdAt: Long, ID: String)

object MessageTypeReference extends TypeReference[Message]
