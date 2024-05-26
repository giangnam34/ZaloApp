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
        try {
            User sender = userRepository.findByPhoneNumber(senderPhoneNumber);
            if (sender == null) return null;

            if (senderPhoneNumber.equals(receiverPhoneNumber)) {
                return null;
            }
            if (!userRepository.existsUserByPhoneNumber(receiverPhoneNumber)) {
                return null;
            }

            GroupChat groupChat = new GroupChat();
            User firstReceiver = userRepository.findByPhoneNumber(receiverPhoneNumber);
            if (firstReceiver == null) throw new IllegalArgumentException("Người nhận đầu tiên không tồn tại!");
            groupChat.setGroupName(firstReceiver.getFullName());
            groupChat.setAvatar(firstReceiver.getImageAvatarUrl());
            groupChat = groupChatRepository.save(groupChat);

            GroupChatUser groupChatSender = new GroupChatUser();
            GroupChatUserId groupChatSenderId = new GroupChatUserId();
            groupChatSenderId.setGroupId(groupChat.getId());
            groupChatSenderId.setPhoneNumberUser(sender.getPhoneNumber());
            groupChatSender.setId(groupChatSenderId);
            groupChatUserRepository.save(groupChatSender);


            User receiver = userRepository.findByPhoneNumber(receiverPhoneNumber);
            GroupChatUser groupChatReceiver = new GroupChatUser();
            GroupChatUserId groupChatReceiverId = new GroupChatUserId();
            groupChatReceiverId.setGroupId(groupChat.getId());
            groupChatReceiverId.setPhoneNumberUser(receiver.getPhoneNumber());
            groupChatReceiver.setId(groupChatReceiverId);
            groupChatUserRepository.save(groupChatReceiver);

            return groupChat.getId();

        } catch (
                Exception e) {
            e.printStackTrace();
            return null;
        }
    }


}
