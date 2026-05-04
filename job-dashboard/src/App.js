import React, { useEffect, useState } from "react";
import "./App.css";
import JobForm from "./Components/JobForm";
import JobTable from "./Components/JobTable";
import StatsCards from "./Components/StatsCards";
import CsvUpload from "./Components/CsvUpload";

function App() {

  const [jobs, setJobs] = useState([]);
  const [jobType, setJobType] = useState("EMAIL");
  const [data, setData] = useState("");
  const [priority, setPriority] = useState("NORMAL");
  const [date, setDate] = useState("");
  const [time, setTime] = useState("");
  const [filter, setFilter] = useState("ALL");

  const loadJobs = async () => {
    const res = await fetch("http://localhost:8080/jobs/all");
    const data = await res.json();
    setJobs(data);
  };

  useEffect(() => {
    loadJobs();
    const interval = setInterval(loadJobs, 2000);
    return () => clearInterval(interval);
  }, []);

  const submitJob = async () => {

    const timestamp =
      date && time
        ? new Date(`${date}T${time}`).getTime()
        : null;

    await fetch("http://localhost:8080/jobs/submit", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify({
        jobType,
        data,
        priority,
        scheduledTime: timestamp
      }),
    });

    setData("");
    setDate("");
    setTime("");
    loadJobs();
  };

  const filteredJobs =
    filter === "ALL"
      ? jobs
      : jobs.filter(job => job.status === filter);

  return (
    <div className="container">

      <JobForm
        jobType={jobType}
        setJobType={setJobType}
        data={data}
        setData={setData}
        priority={priority}
        setPriority={setPriority}
        date={date}
        setDate={setDate}
        time={time}
        setTime={setTime}
        submitJob={submitJob}
      />
      <CsvUpload />

      <StatsCards jobs={jobs} />

      <div className="card">
        <div style={{ display: "flex", gap: "10px", marginBottom: "10px" }}>
          <button className="button" onClick={() => setFilter("ALL")}>ALL</button>
          <button className="button" onClick={() => setFilter("SUCCESS")}>SUCCESS</button>
          <button className="button" onClick={() => setFilter("FAILED")}>FAILED</button>
          <button className="button" onClick={() => setFilter("RUNNING")}>RUNNING</button>
        </div>
      </div>

      <JobTable jobs={filteredJobs} />

    </div>
  );
}

export default App;