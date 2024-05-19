//package com.essay.zaloapp.domain.models;
//
//import lombok.AllArgsConstructor;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//import lombok.Setter;
//
//import javax.persistence.*;
//import java.util.List;
//
//@Entity
//@Table(name = "reply_message")
//@Getter
//@Setter
//@NoArgsConstructor
//@AllArgsConstructor
//public class ReplyMessage {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private int id;
//
//    private String content;
//
//    @ManyToOne
//    @JoinColumn(name = "sender_id")
//    private User sender;
//
//    @OneToOne(mappedBy = "replyMessage", cascade = CascadeType.ALL)
//    private MessageChat messageChat;
//
//    @ManyToMany(cascade = CascadeType.ALL)
//    @JoinTable(name = "post_resource", joinColumns = @JoinColumn(name = "message_id"), inverseJoinColumns = @JoinColumn(name = "resource_id"))
//    private List<Resource> files;
//}
