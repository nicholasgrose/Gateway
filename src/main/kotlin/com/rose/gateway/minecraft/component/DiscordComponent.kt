package com.rose.gateway.minecraft.component

import dev.kord.core.entity.Member
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextColor

/**
 * Creates an at [Component] for a Discord member
 *
 * @param user The user to create an at [Component] for
 * @param userColor The color the user should be in the [Component]
 * @return The at [Component]
 */
fun atMember(
    user: Member,
    userColor: TextColor,
): Component {
    return "@".primaryComponent() + member(user).color(userColor)
}

/**
 * Creates a member [Component] for a Discord [Member]
 *
 * @param user The user to create a [Component] for
 * @return The member [Component]
 */
fun member(user: Member): Component {
    return user.effectiveName.secondaryComponent()
        .showTextOnHover("Username: ".component() + user.username.primaryComponent().italic())
}
