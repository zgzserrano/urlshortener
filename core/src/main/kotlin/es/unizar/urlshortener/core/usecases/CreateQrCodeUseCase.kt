package es.unizar.urlshortener.core.usecases

import es.unizar.urlshortener.core.*
import boofcv.alg.fiducial.qrcode.QrCodeEncoder
import boofcv.alg.fiducial.qrcode.QrCodeGeneratorImage
import java.net.URI

/**
 * Given an url returns the key that is used to create a Qr Code url.
 */
interface CreateQrCodeUseCase {
    fun create(url: URI): QrCode
}

/**
 * Implementation of [CreateQrCodeUseCase].
 */
class CreateQrCodeUseCaseImpl(
    private val qrCodeRepository: QrCodeRepositoryService,
) : CreateQrCodeUseCase {
    override fun create(url: URI): QrCode 
        {
            // Create the QrCode data structure with the url.
            val qr = QrCodeEncoder().addAutomatic(url.toString()).fixate()
            // Render the QR Code into a BoofCV style image
            // 15 = pixelsPerModule (square)
            val generator = QrCodeGeneratorImage(15).render(qr)
            val urlString = url.toString()
            val id: String = urlString.substring(urlString.lastIndexOf("-")+1)
            val qu = QrCode(
                hash = id,
                gray = generator.gray,
            )
            return qrCodeRepository.save(qu)
        }
}
