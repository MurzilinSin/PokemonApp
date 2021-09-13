package com.example.pokemon.model.responce

data class Move(
    val move: MoveX,
    val version_group_details: List<VersionGroupDetail>
)