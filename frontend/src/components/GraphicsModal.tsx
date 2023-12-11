"use client"

import React from "react";
import {
  Modal,
  ModalOverlay,
  ModalContent,
  ModalHeader,
  ModalCloseButton,
  ModalBody,
  ModalFooter,
  Button,
} from "@chakra-ui/react";
import SockJs from 'sockjs-client';
import { over } from 'stompjs';
import { useEffect, useState } from 'react';
import ChartComponent from './ChartComponent';

interface SensorData {
  id: number;
  data: number;
  name: string;
  timestamp: EpochTimeStamp;
}

const GraphicsModal = ({ isOpen, onClose, selectedSensor, setData, dataEdit   }) => {
  const { id, name, machineId } = dataEdit;

  const [chartData, setChartData] = useState<any>(
    {
      labels: [], 
      values: []
    },
  );

  const [sensor, setSensor] = useState<any>(
    {
      data: []
    }
  );

  const [stompClient, setStompClient] = useState<any>();

  const backend_host = process.env.NEXT_PUBLIC_BACKEND_HOST;
  const backend_port = process.env.NEXT_PUBLIC_BACKEND_PORT;

  const connectToWebSocket = async () => {
    const sock = new SockJs(`http://${backend_host}:${backend_port}/ws`);
    const temp = over(sock);
  
    return new Promise((resolve, reject) => {
      temp.connect({}, () => {
        resolve(temp);
      }, (error) => {
        reject(error);
      });
    });
  }
  
  const subscribeToWebSocket = (stompClient: any) => {
    stompClient.subscribe("/topic/public", onMessageReceived);
  }

  const handleClose = () => {
    if (stompClient) {
      stompClient.unsubscribe("/topic/public", onMessageReceived);
      stompClient.disconnect(() => {
        console.log("Disconnected");
      });
    }
    onClose();
  };
  

  useEffect(() => {
    const timestamps = sensor.data.map((dataPoint) => dataPoint.timestamp)
    const values = sensor.data.map((dataPoint) => dataPoint.data)

    setChartData({
      labels: timestamps,
      values: values,
    });
  }, [sensor]);

  const updateSensorData = (incomingData: SensorData) => {
    const date = new Date(incomingData.timestamp);
    const formattedDate = date.toLocaleDateString('pt-BR', {
      day: '2-digit',
      month: '2-digit',
      year: 'numeric',
    }); // Format as "dd/MM/yyyy"
    const formattedTime = date.toLocaleTimeString(); // Format as "HH:MM:SS AM/PM"

    setSensor((prevSensor) => ({
      data: [...prevSensor.data, { timestamp: formattedDate + formattedTime, data: incomingData.data }],
    }));
  };

  const onMessageReceived = (msg: any) => {
    const receivedMessage = JSON.parse(msg.body);

    console.log("received message: ", receivedMessage);

    if (JSON.parse(msg.body).id === id) {
      updateSensorData(receivedMessage);
    }
  }

  useEffect(() => {
    const initializeWebSocket = async () => {
      try {
        const tempStompClient = await connectToWebSocket();
        setStompClient(tempStompClient);
        subscribeToWebSocket(tempStompClient);
      } catch (error) {
        console.error("Error connecting to WebSocket:", error);
      }
    };
  
    initializeWebSocket();
  }, []);

  return (
    <Modal isOpen={isOpen} onClose={handleClose}>
      <ModalOverlay />
      <ModalContent>
        <ModalHeader>Graphics</ModalHeader>
        <ModalCloseButton />
        <ModalBody>

        <ChartComponent data={chartData} />

        </ModalBody>
        <ModalFooter>
          <Button colorScheme="blue" onClick={onClose}>
            Fechar
          </Button>
        </ModalFooter>
      </ModalContent>
    </Modal>
  );
};

export default GraphicsModal;