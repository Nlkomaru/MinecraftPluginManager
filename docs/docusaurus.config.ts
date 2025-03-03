import type * as Preset from "@docusaurus/preset-classic";
import type { Config } from "@docusaurus/types";
import { themes as prismThemes } from "prism-react-renderer";

// This runs in Node.js - Don't use client-side code here (browser APIs, JSX...)

const config: Config = {
    title: "MinecraftPluginManager Documentation",
    favicon: "img/favicon.ico",
    staticDirectories: ["static"],
    trailingSlash: true,

    // Set the production url of your site here
    url: "https://mpm.plugin.nikomaru.dev",
    // Set the /<baseUrl>/ pathname under which your site is served
    // For GitHub pages deployment, it is often '/<projectName>/'
    baseUrl: "/",

    // GitHub pages deployment config.
    // If you aren't using GitHub pages, you don't need these.
    organizationName: "morinoparty", // Usually your GitHub org/user name.
    projectName: "MinecraftPluginManager", // Usually your repo name.

    onBrokenLinks: "throw",
    onBrokenMarkdownLinks: "warn",

    // Even if you don't use internationalization, you can use this field to set
    // useful metadata like html lang. For example, if your site is Chinese, you
    // may want to replace "en" with "zh-Hans".
    i18n: {
        defaultLocale: "ja",
        locales: ["ja"],
    },

    presets: [
        [
            "classic",
            {
                docs: {
                    sidebarPath: "./sidebars.ts",
                    routeBasePath: "",
                    // Please change this to your repo.
                    // Remove this to remove the "edit this page" links.
                    editUrl:
                        "https://github.com/nlkomaru/minecraftpluginmanager/tree/master/docs/",
                },
                theme: {
                    customCss: "./src/css/custom.css",
                },
            } satisfies Preset.Options,
        ],
    ],
    plugins: [
        ["./src/plugins/tailwind-config.js", {}],
        [
            require.resolve("@easyops-cn/docusaurus-search-local"),
            {
                // Options here
                indexDocs: true,
                language: "ja",
                docsRouteBasePath: "/",
            },
        ],
    ],
    themeConfig: {
        // Replace with your project's social card
        image: "img/docusaurus-social-card.jpg",
        navbar: {
            title: "MinecraftPluginManager",
            logo: {
                alt: "MinecraftPluginManager Logo",
                src: "img/logo.svg",
            },
            items: [
                {
                    href: "https://github.com/nlkomaru/minecraftpluginmanager",
                    label: "GitHub",
                    position: "right",
                },
                {
                    href: "https://github.com/nlkomaru/minecraftpluginmanager/releases",
                    label: "Download",
                    position: "right",
                },
                {
                    href: "/dokka",
                    label: "Dokka",
                    position: "left",
                    target: "_blank",
                },
            ],
        },
        footer: {
            style: "dark",
            links: [
                {
                    title: "ドキュメント",
                    items: [
                        {
                            label: "はじめに",
                            to: "/intro",
                        },
                    ],
                },
                {
                    title: "コミュニティ",
                    items: [
                        {
                            label: "ホームページ",
                            href: "https://morino.party",
                        },
                        {
                            label: "Discord",
                            href: "https://discord.com/invite/9HdanPM",
                        },
                        {
                            label: "X",
                            href: "https://x.com/morinoparty",
                        },
                    ],
                },
                {
                    title: "その他",
                    items: [
                        {
                            label: "GitHub",
                            href: "https://github.com/nlkomaru/minecraftpluginmanager",
                        },
                    ],
                },
            ],
            copyright: `No right reserved. This docs under CC0. Built with Docusaurus.`,
        },
        prism: {
            additionalLanguages: [
                "java",
                "groovy",
                "diff",
                "toml",
                "yaml",
                "kotlin",
            ],
            theme: prismThemes.github,
            darkTheme: prismThemes.dracula,
        },
    } satisfies Preset.ThemeConfig,
};

export default config;
