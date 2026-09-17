package com.smartcontrol.pro.ui.profile

enum class AppRole { SUPER_ADMIN, OWNER, ADMIN, MODERATOR, SUPPORT, FINANCE_ADMIN, LEGAL_ADMIN, USER }
fun canAccessAdminPanel(role: AppRole): Boolean = role == AppRole.SUPER_ADMIN || role == AppRole.OWNER
