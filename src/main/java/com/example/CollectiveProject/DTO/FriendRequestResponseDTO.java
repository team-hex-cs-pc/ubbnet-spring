package com.example.CollectiveProject.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FriendRequestResponseDTO {
    private Long id;
    private String senderUsername;
    private String receiverUsername;
}
