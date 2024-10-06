package com.thehuginn.bulletproofs.composite.bytecode

import com.weavechain.zk.bulletproofs.Transcript
import java.io.ByteArrayOutputStream

class TranscriptionWriterInterceptor : ManageableInterceptor() {
    private var commitmentsTranscript = Transcript()
    private var proofsTranscript = Transcript()

    companion object Constants {
        const val TRANSCRIPT_FIELD = "transcript"
        const val TRANSCRIPT_TRANSCRIPT_FIELD = "transcript"  // field inside Transcript
    }

    fun switchToCommitments(instance: Any) = setTranscript(instance, commitmentsTranscript)

    fun switchToProofs(instance: Any) = setTranscript(instance, proofsTranscript)

    fun mergeTranscriptsAndSet(instance: Any) {
        val mergedTranscript = Transcript()

        val commitmentsStream = getTranscriptStream(commitmentsTranscript)
        val proofsStream = getTranscriptStream(proofsTranscript)

        val mergedStream = ByteArrayOutputStream()
        commitmentsStream.writeTo(mergedStream)
        proofsStream.writeTo(mergedStream)

        setTranscriptStream(mergedTranscript, mergedStream)

        setTranscript(instance, mergedTranscript)
    }

    private fun setTranscript(instance: Any, transcript: Transcript) {
        instance.javaClass.getDeclaredField(TRANSCRIPT_FIELD).apply {
            isAccessible = true
            set(instance, transcript)
        }
    }

    private fun getTranscriptStream(transcript: Transcript): ByteArrayOutputStream {
        val field = transcript.javaClass.getDeclaredField(TRANSCRIPT_TRANSCRIPT_FIELD)
        field.isAccessible = true
        return field.get(transcript) as ByteArrayOutputStream
    }

    private fun setTranscriptStream(transcript: Transcript, stream: ByteArrayOutputStream) {
        transcript.javaClass.getDeclaredField(TRANSCRIPT_TRANSCRIPT_FIELD).apply {
            isAccessible = true
            set(transcript, stream)
        }
    }

    override fun stop() {
        commitmentsTranscript = Transcript()
        proofsTranscript = Transcript()
    }

    override fun start() {
        commitmentsTranscript = Transcript()
        proofsTranscript = Transcript()
    }
}