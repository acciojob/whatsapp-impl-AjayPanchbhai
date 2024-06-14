package com.driver;

import java.util.*;

import org.springframework.stereotype.Repository;


@Repository
public class WhatsappRepository {

    //Assume that each user belongs to at most one group
    //You can use the below mentioned hashmaps or delete these and create your own.
    private final HashMap<String, User> users;
    private final HashMap<Group, List<User>> groupUserMap;
    private final HashMap<Group, List<User>> groupPersonalChatMap;
    private final HashMap<Group, List<Message>> groupMessageMap;
    private final HashMap<User, List<Message>> senderMap;
    private final HashMap<Group, User> adminMap;
    private HashSet<String> userMobile;
    private final HashMap<Integer, Message> messages;
    private int customGroupCount;
    private int messageId;

    public WhatsappRepository(){
        this.users = new HashMap<>();
        this.groupMessageMap = new HashMap<Group, List<Message>>();
        this.groupUserMap = new HashMap<Group, List<User>>();
        this.groupPersonalChatMap = new HashMap<Group, List<User>>();
        this.senderMap = new HashMap<User, List<Message>>();
        this.adminMap = new HashMap<Group, User>();
        this.userMobile = new HashSet<>();
        this.messages = new HashMap<>();
        this.customGroupCount = 1;
        this.messageId = 1;
    }

    public String createUser(String name, String mobile) throws Exception {
        if(users.containsKey(mobile)) {
            throw new IllegalArgumentException("User already exists");
        }

        users.put(mobile, new User(name, mobile));
        return "SUCCESS";
    }

    public Group createGroup(List<User> users) {
        //// The list contains at least 2 users where the first user is the admin.
        // If there are only 2 users, the group is a personal chat and the group name should be kept as the name of the second user(other than admin)
        // If there are 2+ users, the name of group should be "Group #count". For example, the name of first group would be "Group 1", second would be "Group 2" and so on.
        // Note that a personal chat is not considered a group and the count is not updated for personal chats.

        Group group = new Group();
        if (users.size() == 2) {
            group.setNumberOfParticipants(2);
            group.setName(users.get(1).getName());

//            groupPersonalChatMap.put(group, users);
        } else {
            String groupName = "Group " + customGroupCount++;
            User admin = users.get(0);

            group.setNumberOfParticipants(users.size());
            group.setName(groupName);

            adminMap.put(group, admin);
        }

        groupMessageMap.put(group, new ArrayList<>());
        groupUserMap.put(group, users);

        return group;
    }

    public int createMessage(String content) {
        // The 'i^th' created message has message id 'i'.

        Message message = new Message();
        message.setId(messageId);

        messages.put(messageId, message);
        return messageId++;
    }

    public int sendMessage(Message message, User sender, Group group) throws Exception {
        //Throw "Group does not exist" if the mentioned group does not exist
        //Throw "You are not allowed to send message" if the sender is not a member of the group
        //If the message is sent successfully, return the final number of messages in that group.

        if(!groupUserMap.containsKey(group)) {
            throw new Exception("Group does not exist");
        }

        for(User user : groupUserMap.get(group)) {
            if(user.equals(sender)) {
                if(!senderMap.containsKey(sender))
                    senderMap.put(sender, new ArrayList<>());

                senderMap.get(sender).add(message);
                groupMessageMap.get(group).add(message);

                return groupMessageMap.get(group).size();
            }
        }

        throw new Exception("You are not allowed to send message");
    }

    public String changeAdmin(User approver, User user, Group group) throws Exception{
        //Throw "Group does not exist" if the mentioned group does not exist
        //Throw "Approver does not have rights" if the approver is not the current admin of the group
        //Throw "User is not a participant" if the user is not a part of the group
        //Change the admin of the group to "user" and return "SUCCESS". Note that at one time there is only one admin and the admin rights are transferred from approver to user.

        if(!groupUserMap.containsKey(group)) {
            throw new Exception("Group does not exist");
        }

        boolean userNotExist = true;

        for(User user1 : groupUserMap.get(group)) {
            if (user1.equals(user)) {
                userNotExist = false;
                break;
            }
        }

        if (userNotExist) throw new Exception("User is not a participant");

        for(User user1 : groupUserMap.get(group)) {
            if(user1.equals(approver)) {
                if (!adminMap.get(group).equals(approver))
                    throw new Exception("Approver does not have rights");
                adminMap.put(group, user);

                break;
            }
        }

        return "SUCCESS";
    }

    public int removeUser(User user) throws Exception{
        //This is a bonus problem and does not contains any marks
        //A user belongs to exactly one group
        //If user is not found in any group, throw "User not found" exception
        //If user is found in a group and it is the admin, throw "Cannot remove admin" exception
        //If user is not the admin, remove the user from the group, remove all its messages from all the databases, and update relevant attributes accordingly.
        //If user is removed successfully, return (the updated number of users in the group + the updated number of messages in group + the updated number of overall messages)
        if(!users.containsKey(user.getMobile()))
            throw new Exception("User not found");

        for(User user1 : adminMap.values()) {
            if(user1.equals(user))
                throw new Exception("Cannot remove admin");
        }
        Group group = null;

        for(Map.Entry<Group, List<User>> entity : groupUserMap.entrySet()) {
            for(User user1 : entity.getValue()) {
                if(user.equals(user1)) {
                    group = entity.getKey();
                    groupUserMap.get(group).remove(user);
                    List<Message> messages1 = senderMap.get(user);
                    groupMessageMap.get(group).removeAll(messages1);

                    for(Message message : messages1) {
                        messages.remove(message.getId());
                        messageId--;
                    }

                    senderMap.get(user).clear();
                    break;
                }
            }
        }

        int cnt = 0;

        for(Map.Entry<User, List<Message>> en : senderMap.entrySet()) {
            cnt += en.getValue().size();
        }

        return group != null ? groupUserMap.get(group).size() + groupMessageMap.get(group).size() + cnt : 0;
    }


    public String findMessage(Date start, Date end, int K) throws Exception {
        //This is a bonus problem and does not contains any marks
        // Find the Kth latest message between start and end (excluding start and end)
        // If the number of messages between given time is less than K, throw "K is greater than the number of messages" exception
        ArrayList<Message> filteredMessages = new ArrayList<>();

        for (Message message : messages.values()) {
            if (message.getTimestamp().after(start) && message.getTimestamp().before(end)) {
                filteredMessages.add(message);
            }
        }

        if (filteredMessages.size() < K) {
            throw new Exception("K is greater than the number of messages");
        }

        filteredMessages.sort(new Comparator<Message>() {
            @Override
            public int compare(Message m1, Message m2) {
                return m2.getTimestamp().compareTo(m1.getTimestamp());
            }
        });

        return filteredMessages.get(K - 1).getContent();
    }
}
