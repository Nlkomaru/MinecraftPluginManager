/*
 * Written in 2023-2024 by Nikomaru <nikomaru@nikomaru.dev>
 *
 * To the extent possible under law, the author(s) have dedicated all copyright and related and neighboring rights to this software to the public domain worldwide.This software is distributed without any warranty.
 *
 * You should have received a copy of the CC0 Public Domain Dedication along with this software.
 * If not, see <http://creativecommons.org/publicdomain/zero/1.0/>.
 */

package dev.nikomaru.minecraftpluginmanager.repository.downloader.github

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GithubRelease(
    @SerialName("url") val url: String,
    @SerialName("assets_url") val assetsUrl: String,
    @SerialName("upload_url") val uploadUrl: String,
    @SerialName("html_url") val htmlUrl: String,
    @SerialName("id") val id: Long,
    @SerialName("tag_name") val tagName: String,
    @SerialName("target_commitish") val targetCommitish: String,
    @SerialName("name") val name: String,
    @SerialName("draft") val draft: Boolean,
    @SerialName("prerelease") val prerelease: Boolean,
    @SerialName("created_at") val createdAt: String,
    @SerialName("published_at") val publishedAt: String,
    @SerialName("assets") val assets: List<GithubAsset>,
)

@Serializable
data class GithubAsset(
    @SerialName("url") val url: String,
    @SerialName("id") val id: Long,
    @SerialName("node_id") val nodeId: String,
    @SerialName("name") val name: String,
    @SerialName("label") val label: String?,
    @SerialName("uploader") val uploader: GithubUploader,
    @SerialName("content_type") val contentType: String,
    @SerialName("state") val state: String,
    @SerialName("size") val size: Long,
    @SerialName("download_count") val downloadCount: Long,
    @SerialName("created_at") val createdAt: String,
    @SerialName("updated_at") val updatedAt: String,
    @SerialName("browser_download_url") val browserDownloadUrl: String
)

@Serializable
data class GithubUploader(
    @SerialName("login") val login: String,
    @SerialName("id") val id: Long,
    @SerialName("node_id") val nodeId: String,
    @SerialName("avatar_url") val avatarUrl: String,
    @SerialName("gravatar_id") val gravatarId: String,
    @SerialName("url") val url: String,
    @SerialName("html_url") val htmlUrl: String,
    @SerialName("followers_url") val followersUrl: String,
    @SerialName("following_url") val followingUrl: String,
    @SerialName("gists_url") val gistsUrl: String,
    @SerialName("starred_url") val starredUrl: String,
    @SerialName("subscriptions_url") val subscriptionsUrl: String,
    @SerialName("organizations_url") val organizationsUrl: String,
    @SerialName("repos_url") val reposUrl: String,
    @SerialName("events_url") val eventsUrl: String,
    @SerialName("received_events_url") val receivedEventsUrl: String,
    @SerialName("type") val type: String,
    @SerialName("site_admin") val siteAdmin: Boolean
)
