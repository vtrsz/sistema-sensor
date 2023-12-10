"use client"

import { useState, useEffect } from 'react';
import SockJs from 'sockjs-client';
import { over } from 'stompjs';


const WebSocketComponent = () => {
  const [stompClient, setStompClient] = useState<any>();
  const [isConnected, setIsConnected] = useState<Boolean>(false);

  const [messages, setMessages] = useState<any[]>([]);
  
  const connect = () => {
    const sock = new SockJs('http://localhost:8080/ws');
    const temp = over(sock);
    setStompClient(temp);

    temp.connect({}, onConnect, onError);
  }

  const onError = (error: any) => {
    console.log(error);
  }

  const onConnect = () => {
    setIsConnected(true);
  }

  
  const onMessageReceived = (msg: any) => {
    const receivedMessage = JSON.parse(msg.body);

    console.log("received message: ", receivedMessage);

    setMessages([...messages, receivedMessage]);
  }

  useEffect(() => {
    console.log("useEffect connected: ", isConnected);
    if (isConnected && stompClient) {
      console.log("connected and subscribed to /topic/public");
      isConnected === true && stompClient.subscribe("/topic/public", onMessageReceived);

      /*
      return () => {
        stompClient.unsubscribe("/topic/public", onMessageReceived);
      }
      */
    }
  }, [isConnected]);

  /*
  useEffect(() => {
    connect();
  }, []);
  */


  const sendMessage = () => {
    if (isConnected && stompClient) {
      console.log("enviando mensagem");
      stompClient.send("/app/message", {}, JSON.stringify("Cliente mandou mensagem!"));  
    }
  };

  const receiveMsg = (msg : any) => {
    const receivedMessage = JSON.parse(msg.body);

    console.log("recebendo mensagem " + receivedMessage);
  }

  const sendSensor1 = () => {
    if (isConnected && stompClient) {
      const sensorData = {
        id: 'sensor1'
      };

      stompClient.send("/app/message", {}, JSON.stringify(sensorData));
    }
  };

  return (<>
  </>);
}

export default WebSocketComponent;