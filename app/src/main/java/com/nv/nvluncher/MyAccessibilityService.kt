package com.nv.nvluncher;

import android.accessibilityservice.AccessibilityService
import android.view.accessibility.AccessibilityEvent

class MyAccessibilityService : AccessibilityService() {

    override fun onInterrupt() {}

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {}
}