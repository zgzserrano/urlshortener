package es.unizar.urlshortener.core.usecases

import es.unizar.urlshortener.core.*
import java.util.Date
import boofcv.alg.fiducial.qrcode.QrCodeEncoder
import boofcv.alg.fiducial.qrcode.QrCodeGeneratorImage
import java.net.URI

/**
 * Given an url returns the key that is used to create a short URL.
 * When the url is created optional data may be added.
 *
 * **Note**: This is an example of functionality.
 */
interface CreateQrCodeUseCase {
    fun create(url: URI): QrCode
}

/**
 * Implementation of [CreateQrCodeUseCase].
 */
class CreateQrCodeUseCaseImpl(
    private val qrCodeRepository: QrCodeRepositoryService,
    private val hashService: HashService
) : CreateQrCodeUseCase {
    override fun create(url: URI): QrCode 
            // Create the QrCode data structure with your message.
        {
            val qr = QrCodeEncoder().addAutomatic(url.toString()).fixate();
            //======================================================================================
            // Render the QR Code into a BoofCV style image. It's also possible to create PDF documents
            // 15 = pixelsPerModule (square)
            val generator = QrCodeGeneratorImage(15).render(qr)
            val id: String = hashService.hasUrl("qr"+url)
            val qu = QrCode(
                hash = id,
                gray = generator.getGray(),
            )
            return qrCodeRepository.save(qu)
        }
}