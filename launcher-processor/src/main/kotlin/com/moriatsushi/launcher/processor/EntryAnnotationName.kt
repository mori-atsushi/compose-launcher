package com.moriatsushi.launcher.processor

import com.moriatsushi.launcher.Entry

internal val EntryAnnotationName = Entry::class.qualifiedName
    ?: error("require qualifiedName of Entry")
