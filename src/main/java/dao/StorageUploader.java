package dao;
import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobContainerClientBuilder;

public class StorageUploader {
    private BlobContainerClient containerClient;

    public StorageUploader( ) {
        this.containerClient = new BlobContainerClientBuilder()
                .connectionString("DefaultEndpointsProtocol=https;AccountName=sorychcsc311storage;AccountKey=FsrZkhMSXpeYoY7MYdoLDwM5VRgEF2gkQonfrTOWGBT/qJk9GSSPNvf5D3e167I6gBz7yWFAujFk+AStTqIW9A==;EndpointSuffix=core.windows.net")
                .containerName("media-files")
                .buildClient();
    }

    public void uploadFile(String filePath, String blobName) {
        BlobClient blobClient = containerClient.getBlobClient(blobName);
        blobClient.uploadFromFile(filePath);
    }
    public BlobContainerClient getContainerClient(){
        return containerClient;
    }
}