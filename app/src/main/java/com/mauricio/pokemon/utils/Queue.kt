package com.mauricio.pokemon.utils

class Queue<T> (list:MutableList<T>){

    var items:MutableList<T> = list

    fun isEmpty():Boolean = items.isEmpty()

    fun size():Int = items.count()

    override  fun toString() = items.toString()

    fun enqueue(element:T){
        items.add(element)
    }

    fun dequeue():T?{
        if (this.isEmpty()){
            return null
        } else {
            return items.removeAt(0)
        }
    }

    fun peek():T?{
        return items[0]
    }
}