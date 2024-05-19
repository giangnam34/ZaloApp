package com.essay.zaloapp.services.impl;

import com.essay.zaloapp.domain.enums.Status;
import com.essay.zaloapp.domain.models.Composite.GroupChatUserId;
import com.essay.zaloapp.domain.models.GroupChat;
import com.essay.zaloapp.domain.models.GroupChatUser;
import com.essay.zaloapp.domain.models.User;
import com.essay.zaloapp.repository.GroupChatRepository;
import com.essay.zaloapp.repository.GroupChatUserRepository;
import com.essay.zaloapp.repository.MessageChatRepository;
import com.essay.zaloapp.repository.UserRepository;
import com.essay.zaloapp.services.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private GroupChatRepository groupChatRepository;

    @Autowired
    private GroupChatUserRepository groupChatUserRepository;

    @Autowired
    private MessageChatRepository messageChatRepository;

    @Override
    public void connect(String phoneNumber) {
        var storedUser = userRepository.findByPhoneNumber(phoneNumber);
        if (storedUser != null) {
            storedUser.setStatus(Status.ONLINE);
            userRepository.save(storedUser);
        }
    }

    @Override
    public void disconnect(String phoneNumber) {
        var storedUser = userRepository.findByPhoneNumber(phoneNumber);
        if (storedUser != null) {
            storedUser.setStatus(Status.OFFLINE);
            userRepository.save(storedUser);
        }
    }

    @Override
    public List<User> findConnectedUsers() {
        return userRepository.findAllByStatus(Status.ONLINE);
    }

    @Override
    public Long getChatRoomId(String senderPhoneNumber,
                              String receiverPhoneNumber) {
        Optional<GroupChat> opGroupChat = groupChatRepository.findById(groupChatUserRepository.findGroupChatIdsBySenderAndReceiverPhoneNumbers(senderPhoneNumber, receiverPhoneNumber).get(0));
        if (opGroupChat.isPresent()) {
            return opGroupChat.get().getId();
        } else {
            return createChatRoom(senderPhoneNumber, receiverPhoneNumber);
        }
    }

    @Override
    public Long createChatRoom(String senderPhoneNumber, String receiverPhoneNumber) {
        User sender = userRepository.findByPhoneNumber(senderPhoneNumber);
        User receiver = userRepository.findByPhoneNumber(receiverPhoneNumber);
        GroupChat groupChat = new GroupChat();
        groupChat.setGroupName(sender.getFullName() + "_" + receiver.getFullName());
        GroupChat groupChatSaved = groupChatRepository.save(groupChat);
        GroupChatUser gcu1 = new GroupChatUser();
        gcu1.setId(new GroupChatUserId(groupChatSaved.getId(), senderPhoneNumber));
        groupChatUserRepository.save(gcu1);
        GroupChatUser gcu2 = new GroupChatUser();
        gcu2.setId(new GroupChatUserId(groupChatSaved.getId(), receiverPhoneNumber));
        groupChatUserRepository.save(gcu2);
        return groupChatSaved.getId();
    }


}
