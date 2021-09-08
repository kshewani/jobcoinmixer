package interfaces;

/**
 * An interface representing MixingRequestService.
 */
public interface IMixingRequestService {
    /**
     * Submits a mixing request to mixing service.
     * @param mixingRequest the mixing request.
     */
    void submitMixingRequest(IMixingRequest mixingRequest);
}
