package br.com.torresmath.key.manager.pix

import br.com.torresmath.key.manager.DeleteKeyGrpcServiceGrpc
import br.com.torresmath.key.manager.GenerateKeyGrpcServiceGrpc
import br.com.torresmath.key.manager.ListKeyGrpcServiceGrpc
import br.com.torresmath.key.manager.RetrieveKeyGrpcServiceGrpc
import io.grpc.ManagedChannel
import io.micronaut.context.annotation.Factory
import io.micronaut.grpc.annotation.GrpcChannel
import javax.inject.Singleton

@Factory
class KeyManagerGrpcFactory(@GrpcChannel("keyManager") val channel: ManagedChannel) {

    @Singleton
    fun generateKey() = GenerateKeyGrpcServiceGrpc.newBlockingStub(channel)

    @Singleton
    fun deleteKey() =  DeleteKeyGrpcServiceGrpc.newBlockingStub(channel)

    @Singleton
    fun retrieveKey() = RetrieveKeyGrpcServiceGrpc.newBlockingStub(channel)

    @Singleton
    fun listKey() = ListKeyGrpcServiceGrpc.newBlockingStub(channel)
}