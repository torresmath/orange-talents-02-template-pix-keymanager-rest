package br.com.torresmath.key.manager.pix.model

enum class KeyType(val grpcValue: br.com.torresmath.key.manager.KeyType) {
    CPF(br.com.torresmath.key.manager.KeyType.CPF),
    MOBILE_NUMBER(br.com.torresmath.key.manager.KeyType.MOBILE_NUMBER),
    EMAIL(br.com.torresmath.key.manager.KeyType.EMAIL),
    RANDOM(br.com.torresmath.key.manager.KeyType.RANDOM);
}