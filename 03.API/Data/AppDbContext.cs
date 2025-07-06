using Microsoft.EntityFrameworkCore;
using API.CongViec.Models;
using API.CongViec.Model;
using System.Numerics;

namespace API.CongViec.Data
{
    public class AppDbContext : DbContext
    {
        public AppDbContext(DbContextOptions<AppDbContext> options) : base(options) { }

        public DbSet<User> Users { get; set; }
        public DbSet<Plan> Plans { get; set; }        // Đổi sang Plan
        public DbSet<TaskModel> Tasks { get; set; }

        protected override void OnModelCreating(ModelBuilder modelBuilder)
        {
            base.OnModelCreating(modelBuilder);

            modelBuilder.Entity<User>().ToTable("Users");
            modelBuilder.Entity<Plan>().ToTable("Plans");           // Đổi sang Plan
            modelBuilder.Entity<TaskModel>().ToTable("Tasks");

            // RÀNG BUỘC QUAN HỆ
            modelBuilder.Entity<User>()
                .HasMany(u => u.Plans)
                .WithOne(p => p.User)
                .HasForeignKey(p => p.UserId);

            modelBuilder.Entity<Plan>()
                .HasMany(p => p.Tasks)
                .WithOne(t => t.Plan)
                .HasForeignKey(t => t.PlanId);
        }
    }
}
