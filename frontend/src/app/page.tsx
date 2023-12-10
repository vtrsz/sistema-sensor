import SensorSelector from "@/components/SensorSelector";

const Home: React.FC = () => {
  return (
    <>
      <main className="flex flex-col items-center justify-between min-h-screen pt-4">
        <section className="flex items-center justify-center w-full min-h-[400px] p-2 ">
          <SensorSelector/>
        </section>
      </main>
    </>
  );
};

export default Home;