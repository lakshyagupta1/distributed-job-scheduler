import React from "react";

function JobForm({
  jobType,
  setJobType,
  data,
  setData,
  priority,
  setPriority,
  date,
  setDate,
  time,
  setTime,
  submitJob
}) {
  
  return (<div className="card">
  <div className="title">Submit Job</div>

  <div style={{ display: "flex", gap: "10px", flexWrap: "wrap", alignItems: "center" }}>

    <select value={jobType} onChange={(e) => setJobType(e.target.value)}>
      <option value="EMAIL">EMAIL</option>
      <option value="FILE">FILE</option>
      <option value="LOG">LOG</option>
    </select>

    <input
      className="input"
      placeholder="Enter data"
      value={data}
      onChange={(e) => setData(e.target.value)}
    />

    <select value={priority} onChange={(e) => setPriority(e.target.value)}>
      <option value="HIGH">HIGH</option>
      <option value="NORMAL">NORMAL</option>
      <option value="LOW">LOW</option>
    </select>

    <input
      type="date"
      value={date}
      onChange={(e) => setDate(e.target.value)}
      className="input"
    />

    <input
      type="time"
      value={time}
      onChange={(e) => setTime(e.target.value)}
      className="input"
    />

    <button className="button" onClick={submitJob}>
      Submit Job
    </button>

  </div>
</div>
  );
}

export default JobForm;