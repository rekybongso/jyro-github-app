package com.rekyb.jyro.data.remote.mapper

import com.rekyb.jyro.data.remote.response.UserItemsDto
import com.rekyb.jyro.domain.model.UserItemsModel
import com.rekyb.jyro.utils.EntityMapper

class UserItemsMapper : EntityMapper<UserItemsDto, UserItemsModel> {
    override fun mapFromEntity(entity: UserItemsDto): UserItemsModel {
        return UserItemsModel(
            userName = entity.login,
            userId = entity.id,
            userAvatarUrl = entity.avatarUrl,
            userProfileUrl = entity.url
        )
    }

    override fun mapToEntity(model: UserItemsModel): UserItemsDto {
        return UserItemsDto(
            login = model.userName,
            id = model.userId,
            avatarUrl = model.userAvatarUrl,
            url = model.userProfileUrl
        )
    }

    fun toDomainList(initial: List<UserItemsModel>): List<UserItemsDto> {
        return initial.map { mapToEntity(it) }
    }

    fun fromDomainList(initial: List<UserItemsDto>): List<UserItemsModel> {
        return initial.map { mapFromEntity(it) }
    }
}
