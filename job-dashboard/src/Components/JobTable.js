import React from "react";

function JobTable({ jobs }) {
  return (
    <div className="card">
      <div className="title">Jobs</div>

      <table className="table">
        <thead>
          <tr>
            <th>ID</th>
            <th>Type</th>
            <th>Data</th>
            <th>Status</th>
            <th>Scheduled</th>
            <th>Priority</th>
            <th>Retries</th>
            <th>Results</th>
          </tr>
        </thead>

        <tbody>
          {jobs.map((job) => (
            <tr key={job.id}>
              <td>{job.id}</td>
              <td>{job.jobType}</td>
              <td>{job.data}</td>
              

              <td className={
                job.status === "SUCCESS"
                  ? "status-success"
                  : job.status === "FAILED"
                  ? "status-failed"
                  : job.status === "RUNNING"
                  ? "status-running"
                  : ""
              }>
                {job.status}
              </td>

              <td>
                {job.scheduledTime
                  ? new Date(job.scheduledTime).toLocaleString()
                  : "Immediate"}
              </td>

              <td>{job.priority}</td>
              <td>{job.retryCount}</td>
              <td>{job.result}</td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
}

export default JobTable;