package com.gaspersoft.unilab.domain.model

sealed interface OperationResult<out T> {
    data class Success<T>(val data: T) : OperationResult<T>

    data class Failure(val message: String) : OperationResult<Nothing>
}
