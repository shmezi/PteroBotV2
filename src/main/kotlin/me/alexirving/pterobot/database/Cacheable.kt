package me.alexirving.pterobot.database

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import java.util.*

abstract class Cacheable<T:Any> : Cloneable {


    @JsonProperty
    var identifier: String


    constructor() {
        this.identifier = UUID.randomUUID().toString()
    }

    constructor(id: T) {
        this.identifier = id.toString()
    }

    @JsonIgnore
    fun getIdentifierAsUUID() = UUID.fromString(identifier)

    public override fun clone(): Cacheable<T> {
        return super.clone() as Cacheable<T>
    }
}