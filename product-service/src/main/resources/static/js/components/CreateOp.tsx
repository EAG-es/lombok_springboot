import React from 'react';

const CreateComponent: React.FC = () => {
    return (
        <div className="p-4 border border-primary rounded bg-light shadow-sm">
            <h4 className="text-primary mb-3">Create New Item</h4>
            <div className="mb-3">
                <label className="form-label">Name</label>
                <input type="text" className="form-control" placeholder="Enter name..." />
            </div>
            <button className="btn btn-primary">Submit</button>
        </div>
    );
};

export default CreateComponent;
