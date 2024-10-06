package com.thehuginn.bulletproofs.composite

import net.bytebuddy.agent.ByteBuddyAgent
import org.junit.jupiter.api.extension.BeforeAllCallback
import org.junit.jupiter.api.extension.ExtensionContext

class ByteBuddyExtension : BeforeAllCallback {

    override fun beforeAll(p0: ExtensionContext?) {
        ByteBuddyAgent.install()
        CompositeApiSetup.premain(null, ByteBuddyAgent.getInstrumentation())
    }

}