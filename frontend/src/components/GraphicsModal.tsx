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

interface SensorGraphicData {
  timestamp: EpochTimeStamp;
  data: number;
}

interface Sensor {
  name: string;
  data: SensorGraphicData[];
}

const GraphicsModal = ({ isOpen, onClose, selectedSensor, setData, dataEdit   }) => {
  const { id, name, machineId } = dataEdit;

  const [chartData, setChartData] = useState<any>(
    {
      labels: [], 
      values: []
    },
  );

  const [stompClient, setStompClient] = useState<any>();
  const [isConnected, setIsConnected] = useState<Boolean>(false);

  const connect = () => {
    const sock = new SockJs('http://backend_host:backend_port/ws');
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

  const [sensors, setSensors] = useState<any[]>([
    //TODO: create a option to select sensors in frontend
    {
      name: "sensor1",
      data: []
    },
    {
      name: "sensor2",
      data: []
    },
    {
      name: "sensor3",
      data: []
    },
  ]);

  useEffect(() => {
    const timestamps = sensors.flatMap((sensor) =>
      sensor.data.map((dataPoint) => dataPoint.timestamp)
    );

    const values = sensors.flatMap((sensor) =>
      sensor.data.map((dataPoint) => dataPoint.data)
    );

    setChartData({
      labels: timestamps,
      values: values,
    });
  }, [sensors]);

  const updateSensorData = (incomingData: SensorData, prevSensors: Sensor[]) => {
    const date = new Date(incomingData.timestamp);
    const formattedDate = date.toLocaleDateString('pt-BR', {
      day: '2-digit',
      month: '2-digit',
      year: 'numeric',
    }); // Format as "dd/MM/yyyy"
    const formattedTime = date.toLocaleTimeString(); // Format as "HH:MM:SS AM/PM"

    setSensors((prevSensors) =>
      prevSensors.map((sensor) =>
        sensor.name === incomingData.name
          ? {
              ...sensor,
              data: [...sensor.data, { timestamp: formattedDate + formattedTime, data: incomingData.data }],
            }
          : sensor
      )
    );
  };

  const onMessageReceived = (msg: any) => {
    const receivedMessage = JSON.parse(msg.body);

    console.log("received message: ", receivedMessage);

    if (JSON.parse(msg.body).id === id) {
      console.log("received message is not from selected sensor");
      return;
    }

    updateSensorData(receivedMessage);
  }

  useEffect(() => {
    console.log("useEffect connected: ", isConnected);
    if (isConnected && stompClient) {
      const sleepDuration = 5000; // 5 seconds

      console.log("connected and subscribed to /topic/public");
      while (isConnected === false) {
        setTimeout(() => {
          console.log('Sleep complete!');
        }, sleepDuration);
      }
      isConnected === true && stompClient.subscribe("/topic/public", onMessageReceived);

      
      return () => {
        stompClient.unsubscribe("/topic/public", onMessageReceived);
      }
    }
  }, [isConnected]);

  useEffect(() => {
    connect();
  }, []);

  const handleClose = () => {
    stompClient.disconnect();
    setIsConnected(false);
    onClose();
  };

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