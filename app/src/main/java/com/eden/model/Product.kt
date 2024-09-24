package com.eden.model

class Product {
    var id: Long = 0
    var conditionTypeId: Long = 0
    var usersId: Long = 0
    @kotlin.jvm.JvmField
    var name: String? = null
    var value: Float = 0f
    var description: String? = null
    var urlImage: String? = null
    var avaliation: Float = 0f
    var stock: Int = 0

    constructor()

    constructor(
        id: Long,
        conditionTypeId: Long,
        usersId: Long,
        name: String?,
        value: Float,
        description: String?,
        urlImage: String?,
        avaliation: Float,
        stock: Int
    ) {
        this.id = id
        this.conditionTypeId = conditionTypeId
        this.usersId = usersId
        this.name = name
        this.value = value
        this.description = description
        this.urlImage = urlImage
        this.avaliation = avaliation
        this.stock = stock
    }
}
