package br.com.torresmath.key.manager.pix.model

import br.com.torresmath.key.manager.KeyType as GrpcKeyType

enum class KeyType(val grpcValue: GrpcKeyType) {
    CPF(GrpcKeyType.CPF),
    MOBILE_NUMBER(GrpcKeyType.MOBILE_NUMBER),
    EMAIL(GrpcKeyType.EMAIL),
    RANDOM(GrpcKeyType.RANDOM);
}