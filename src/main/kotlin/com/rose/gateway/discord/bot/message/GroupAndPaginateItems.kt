package com.rose.gateway.discord.bot.message

import com.kotlindiscord.kord.extensions.commands.application.slash.EphemeralSlashCommandContext
import com.kotlindiscord.kord.extensions.pagination.builders.PaginatorBuilder
import com.kotlindiscord.kord.extensions.types.editingPaginator
import com.kotlindiscord.kord.extensions.types.respond
import com.rose.gateway.shared.collections.group
import dev.kord.rest.builder.message.create.FollowupMessageCreateBuilder

/**
 * Groups the items of a collection and then displays them in a paginator if any exist
 *
 * @param T The type of the items to display
 * @param displayItems The items to display
 * @param pageGroupSize The size of groups to create from the display items
 * @param noItemsResponseBuilder Builder that runs if there are no display items
 * @param groupPageBuilder Builder that runs for each group of display items
 * @receiver Response builder
 * @receiver Editing paginator builder
 *
 * @see group
 * @see editingPaginator
 */
suspend fun <T : Any> EphemeralSlashCommandContext<*, *>.groupAndPaginateItems(
    displayItems: Collection<T>,
    pageGroupSize: Int,
    noItemsResponseBuilder: FollowupMessageCreateBuilder.() -> Unit,
    groupPageBuilder: PaginatorBuilder.(Int, List<T>) -> Unit,
) {
    if (displayItems.isEmpty()) {
        respond {
            noItemsResponseBuilder()
        }

        return
    }

    val groupings = displayItems.group(pageGroupSize)

    editingPaginator {
        for ((groupIndex, group) in groupings.withIndex()) {
            groupPageBuilder(groupIndex, group)
        }
    }.send()
}
